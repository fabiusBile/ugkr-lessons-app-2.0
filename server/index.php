<?php
  include('./easy-php-dom.php');

  function getLinksWithAct($act,$date){
    $html = file_get_html("http://study.ugkr.ru/rasp.php?&act=$act&date=$date");
    $linkAct=$act+1;
    $list = array();
    foreach($html->find("a[href^=?act=$linkAct]") as $row){
      $re = $act == 0 ? '/(?<=group=)(.*)/' : '/(?<=prep=)(.*)/';
      preg_match($re, $row->href,$matches);
      if ($matches){
        $name = $row->plaintext;
        array_push($list,array("name"=>$name, "code"=>$matches[0]));
      }
    }
    return $list;
  }

  function getSchedule($act,$code,$date=null){
    if (!$date)
      $date = date("Y-m-d");
    $type = $act == 1 ? "group" : "prep";
    $html = file_get_html("http://study.ugkr.ru/rasp.php?&act=$act&$type=$code&date=$date");
    $schedule = array();
    if ($type == "group"){
      $re = "/(<span style='color:#0033FF' >).*?(<br>)/";
    } else {
      $re = "/<span class=prep_rasp_para>.*?<br>/";
    }
    preg_match_all($re, $html, $matches);
    foreach ($matches[0] as $row){
      $row = strip_tags($row);
      $num = $row[0];
      $lesson = substr($row, 2);
      $lesson = trim($lesson);
      array_push($schedule,array('num' => $num, 'lesson' => $lesson ));
    }
    return $schedule;
  }

  function getLinks($date=null){
    if (!$date)
      $date = date("Y-m-d");
    $answer=array();
    $answer["teachers"] = getLinksWithAct(3,$date);
    $answer["groups"] = getLinksWithAct(0,$date);
    return $answer;
  }

  function checkAnswer($answer){
    return $answer["teachers"]&&$answer["groups"];
  }

  $answer=array();
  $requestCorrect = false;

  if (isset($_GET['action'])){
    switch ($_GET['action']){
      case 'getLinks':
        $answer = getLinks();
        if (!checkAnswer($answer)){
          $tomorrow = (new DateTime('tomorrow'))->format('Y-m-d');
          $answer=getLinks($tomorrow);
          if (!checkAnswer($answer)){
            if (date('m') > 9){
              $september = Date('Y')."-09-01";
            } else {
              $september =  (Date('Y')-1)."-09-01";
            }
            $answer = getLinks($september);
          }
        }
        $requestCorrect = checkAnswer($answer);
      break;
      case 'getSchedule':
        if (isset($_GET['act']) && isset($_GET['code'])){
          $date = isset($_GET['date']) ? $_GET['date'] : null;
          $answer = getSchedule($_GET['act'],$_GET['code'],$date);
          if ($answer)
            $requestCorrect = true;
        }
      break;
      }
  }

  if ($requestCorrect){
    header('Content-Type: application/json');
    echo preg_replace('!\s+!', ' ', json_encode($answer));
  } else {
    echo "NO_SCHEDULE";
  }
  //getSchedule(1,"060fdecd-ea3c-11e4-be7a-485ab63c1e34" );

?>
