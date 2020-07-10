package com.example.itp4501assignment;

import android.provider.BaseColumns;

public class QuestionsLog {
    public QuestionsLog() {
    }

    public static class QuestionsLogEntry implements BaseColumns {
        public static final String TABLE_NAME = "questionslog";

        public static final String COLUMN_NAME_QUESTIONNO = "questionno";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_YOURANSWER = "youranswer";
        public static final String COLUMN_NAME_ISCORRECT = "iscorrect";

    }
}
