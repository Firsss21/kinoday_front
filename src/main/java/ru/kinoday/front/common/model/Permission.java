package ru.kinoday.front.common.model;

public enum Permission {
    WORKER_WRITE("worker:write"),
    WORKER_READ("worker:read");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
