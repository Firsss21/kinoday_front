package firsov.study.securitySpring.model;

public enum Permission {
    WORKER_WRITE("worker:read"),
    WORKER_READ("worker:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
