package za.co.garycampbell.mysticbrewuser;

import java.util.HashMap;
import java.util.List;

import za.co.garycampbell.mysticbrewuser.models.OrdersModel;
import za.co.garycampbell.mysticbrewuser.models.UserModel;

public class Prevalent {

    public static UserModel currentOnlineUser;

    public static final String userEmailKey = "userEmail";

    public static final String userPasswordKey = "userPassword";

    public static final String userLocationAcceptKey = "userLocationAccept";
    public static final String keepMeLoggedIn = "false";

    public static final String currentProductKey = "productID";
    public static final String currentBusinessKey = "businessID";

    //Temp Cart Details
    public static final String currentCartTotalPrice = "cartTotal";
    public static final String currentCartTotalProducts = "productTotal";
    public static final String currentCartID = "cartID";

}
