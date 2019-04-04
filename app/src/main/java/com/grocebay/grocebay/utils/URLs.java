package com.grocebay.grocebay.utils;

public class URLs {

    private static final String ROOT_URL = "http://sleepygamers.xyz/grocebay";


    public static final String URL_REGISTER = ROOT_URL + "/api/user/register.php";
    public static final String URL_LOGIN = ROOT_URL + "/api/user/login.php";
    public static final String GET_PRODUCTS = ROOT_URL + "/api/products.php?apicall=get";
    public static final String ADD_ORDER = ROOT_URL + "/api/order.php?apicall=add";
    public static final String GET_ORDERS = ROOT_URL + "/api/order.php?apicall=getuserorders&status=all&user_id=";

}
