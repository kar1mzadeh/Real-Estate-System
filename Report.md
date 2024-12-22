# Design Rationale  

The design decisions for the `Buyer` and `Agent` classes in the real estate application are in object-oriented principles and design patterns.  

### Inheritance

Inheritance is used to create the `Buyer` and `Agent` classes as specific forms of the `User` class. This decision aligns with the **Open/Closed Principle (OCP)**, which states that a system should be open to extension but closed to modification. By defining shared attributes and methods in the `User` class, we avoid code duplication and provide a consistent framework for user roles.  

Using inheritance also reflects real-world relationships. Buyers and agents are specific types of users with distinct behaviors, and this natural hierarchy is captured in the design. 

### SRP  

The **Single Responsibility Principle (SRP)** highlights the separation of responsibilities in the `Buyer` and `Agent` classes. Buyers focus on actions such as searching for properties and making payments, while agents manage property-related requests. This clear difference reduces dependencies between classes and makes it easier to add or modify features in the future.  

For example, if we need to add a feature where agents can schedule property visits, it will belong to the `Agent` class without impacting the `Buyer` class. This reduces the risk of  errors in unrelated parts of the system. 

### Flexibility 

The system is designed to be flexible in accommodating new requirements. For instance, the menu structure in both classes is implemented as an extensible method. This ensures that future options can be added without rewriting the entire method.  

Moreover, the use of file operations for storing and retrieving user data was chosen for simplicity and independence from external systems during the initial development. 

### Composition Was Avoided

While composition is often a preferred approach in object-oriented design, it was avoided for the `Buyer` and `Agent` roles because their behaviors are tightly coupled with their identity as users. For example, a buyer’s ability to search for properties and make payments is inseparable from their role as a user in the system. Using inheritance instead of composition reduces unnecessary indirection and simplifies the code.  

### Adherence to User-Centric Patterns 

The menu interaction model for both `Buyer` and `Agent` aligns with the **Command Pattern**, where each menu option corresponds to a specific command or action. This design was chosen for its clarity and it makes the code straightforward to add new commands in the future. For instance, if a "View Favorites" feature is added for buyers, it can be easily integrated into the menu structure without disrupting other features.  

In conclusion, the design of the `Buyer` and `Agent` classes is written suitable to object-oriented principles and long-term adaptability. Each decision was made to balance clarity, flexibility, and maintainability, and the system meets current needs while remaining prepared for future growth.  
