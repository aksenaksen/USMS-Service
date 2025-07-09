package io.aksenaksen.demo.usms.member.domain;

public enum MemberRole {
    OWNER("OWNER")
    ,ADMIN("ADMIN");

    private final String roleName;

    MemberRole(String role){
        this.roleName = role;
    }

    public static MemberRole from(String role) {
        for (MemberRole memberRole : values()) {
            if (memberRole.roleName.equals(role)) {
                return memberRole;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }



    public String getRole(){
        return roleName;
    }

    public String getRoleWithPrefix() {
        return "ROLE_" + getRole();
    }
}
