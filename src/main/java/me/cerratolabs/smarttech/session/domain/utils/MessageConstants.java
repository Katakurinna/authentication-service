package me.cerratolabs.smarttech.session.domain.utils;

public class MessageConstants {

    public static final String ACCOUNT_NULL_ERROR = "Account data must be filled.";
    public static final String EMAIL_NULL_ERROR = "Mail is not filled.";
    public static final String USERNAME_NULL_ERROR = "Username is not filled.";
    public static final String EMAIL_OR_USERNAME_NULL_ERROR = "Mail or Username is not filled.";
    public static final String PASSWORD_NULL_ERROR = "Password is not filled.";
    public static final String GENERIC_LOGIN_CIPHER_ERROR = "Some error occurred during authenticating the account.";
    public static final String GENERIC_CREATING_CIPHER_ERROR = "Some error occurred during creating the account.";
    public static final String ACCOUNT_ALREADY_EXIST_ERROR = "Account with this email or username already exist.";
    public static final String SALT_NULL_ERROR = "Password salt is necessary for encrypt the password.";
    public static final String ACCOUNT_NOT_FOUND = "Account not found";

}