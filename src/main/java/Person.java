public class Person {
    // Set nonmandatory fields to empty string
    // to avoid null pointer exceptions
    private String firstName;
    private String middleName = "";
    private String lastName;
    private String email;
    private String namePrefix = "";
    private String nameSuffix = "";

    // No-arg constructor for Firebase
    public Person() {
        // Default constructor for Firebase
    }

    // Constructors with overloading
    public Person(String firstName, String middleName, String lastName, String email, String namePrefix, String nameSuffix) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.namePrefix = namePrefix;
        this.nameSuffix = nameSuffix;
    }

    public Person(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Person(String firstName, String midelName, String lastName, String email) {
        this.firstName = firstName;
        this.middleName = midelName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }
}
