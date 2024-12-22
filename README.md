# csci3509-2024
Skeleton/template code for students of CSCI3509 to develop on.

## Steps to Get Started  

1. **Run the RealEstateApp**  
   - Start by running the `RealEstateApp` class.  

2. **Choose an Option**  
   - Once the app starts, you will see a menu with options:  
     - **Login**: 
     - **Register**:  
     - **Forgot Password**: User can click forgot password. If Email & username is correctly mentioned, the message goes to admin to check and accept the with, if he/she wants.
     - **Exit**: 

3. **User Roles**  
   - After logging in, your role will determine the available actions:  
     - **Buyer**: Search properties, contact sellers, make payments.  
     - **Agent**: Manage requests and assist buyers.  
     - **Admin**: View accounts and handle password requests. If any user requested password view, admin can choose to or not accept anyone's wish, and if so, user can see it if click "forgot password" again.
     - **Seller**: Manage listed properties.  

4. **Data Storage**  
   - The application uses files like `users.csv` to store user information.  

5. **Exit the Application**  
   - To exit, simply choose the "Exit" option from the main menu or logout from your role-specific menu.  

## Notes  
- Ensure the necessary files (`users.csv`, `properties.csv`, etc.) are in place.  
- If you encounter any issues, check the console for error messages.  
