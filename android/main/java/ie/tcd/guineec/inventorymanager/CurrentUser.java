package ie.tcd.guineec.inventorymanager;

/*
To assign user information (String id, String name, String email) to the CurrentUser class:
*N.B* Must assign values before attempting to return values from this class **

        CurrentUser.setCurrentUser("3", "Dylan Walsh", "mail@mail.com");

To get the value:
            1) Delcare a static variable to assign the User object to, within the class you will be using it in:

                         private static User currentUser;

            2)When initialised (else error as currentUser will be null) returns User object:

                        currentUser = CurrentUser.getCurrentUser();

To check if the currentUser has been initialised:

         returns boolean value : CurrentUser.isCurrentUserInit();
*/

public class CurrentUser {

    private static String id,name,email;

    //return currentUser User object if all values are initialised.
    public static User getCurrentUser() {
        if (isCurrentUserInit()) {
            User currentUser = new User(id, name, email);
            return currentUser;
        }
        return null;
    }

    //check if CurrentUser variables have all been initialised
    public static boolean isCurrentUserInit(){
        return id != null && name != null && email != null;
    }

    //initialise all the CurrentUser variables
    public static void setCurrentUser(String userId, String userName, String userEmail) {
        id = userId;
        name = userName;
        email = userEmail;
    }
}
