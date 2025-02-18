package model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Notification {
    private long id;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createdDatetime;

    private User user;
    private Order order;


    private Notification(Builder builder) {
        this.id = builder.id;
        this.message = builder.message;
        this.createdDatetime = builder.createdDatetime;
        this.user = builder.user;
        this.order = builder.order;
    }

    public static class Builder {
        private long id;
        private String message;
        private Timestamp createdDatetime;
        private User user;
        private Order order;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setCreatedDatetime(Timestamp createdDatetime) {
            this.createdDatetime = createdDatetime;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}