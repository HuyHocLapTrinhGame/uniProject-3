/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package constants;

public enum Role {
    ADMIN(0),
    SELLER(1),
    BUYER(2),
    MARKETING(3),
    ACCOUNTANT(4),
    DELIVERY(5),
    CUSTOMER_SUPPORT(6);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Tạo từ giá trị số trong DB
    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Role does not have value: " + value);
    }
}