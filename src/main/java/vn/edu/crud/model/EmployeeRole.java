package vn.edu.crud.model;

import java.math.BigDecimal;

public enum EmployeeRole {
    // Cập nhật lại mức lương tháng theo yêu cầu
    CHAM_HOA("Chăm sóc hoa", new BigDecimal("5500000")),
    GIAO_HANG("Giao hàng", new BigDecimal("6000000")),
    THU_NGAN("Thu ngân", new BigDecimal("5000000")),
    QUAN_LY("Quản lý", new BigDecimal("10000000")); // Thêm Quản lý cho đủ bộ

    private final String label;
    private final BigDecimal luongCoBan;

    EmployeeRole(String label, BigDecimal luongCoBan) {
        this.label = label;
        this.luongCoBan = luongCoBan;
    }

    public String getLabel() { return label; }
    public BigDecimal getLuongCoBan() { return luongCoBan; }

    // Hàm tiện ích: Dịch từ DB ra tên hiển thị
    public static String getLabelFromString(String roleStr) {
        if (roleStr == null) return "";
        for (EmployeeRole role : values()) {
            if (role.name().equalsIgnoreCase(roleStr) || role.label.equalsIgnoreCase(roleStr)) {
                return role.label;
            }
        }
        return roleStr; // Nếu không khớp thì giữ nguyên chữ trong DB
    }
}