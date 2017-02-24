package com.ugkr.lessons;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fabiusbile on 10.02.17.
 */

public class Lesson implements Parcelable {
    String lesson, num;

    public Lesson(String num,String lesson) {
        this.lesson = lesson;
        this.num = num;
    }

    protected Lesson(Parcel in) {
        num = in.readString();
        lesson = in.readString();
    }

    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        @Override
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        @Override
        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(num);
        dest.writeString(lesson);
    }
}
