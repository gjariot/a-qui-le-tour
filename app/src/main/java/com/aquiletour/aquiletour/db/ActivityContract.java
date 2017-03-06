package com.aquiletour.aquiletour.db;

import android.provider.BaseColumns;

public final class ActivityContract {
    private ActivityContract() {}

    public static class Activity implements BaseColumns {
        public static final String TABLE_NAME = "activity";
        public static final String COLUMN_NAME_LABEL = "label";
    }

    public static class Participant implements BaseColumns {
        public static final String TABLE_NAME = "participant";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static class ActivityParticipants implements BaseColumns {
        public static final String TABLE_NAME = "activity_participant";
        public static final String COLUMN_NAME_ACTIVITY = "activity_id";
        public static final String COLUMN_NAME_PARTICIPANT = "participant_id";
    }
}
