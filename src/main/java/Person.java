public class Person {
    // Set nonmandatory fields to empty string
    // to avoid null pointer exceptions
    private String firstName;
    private String middleName = "";
    private String lastName;
    private String email;
    private String namePrefix = "";
    private String nameSuffix = "";

    /** No-arg constructor for Firebase */
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

    public Person(String firstName, String middleName, String lastName, String email) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }

    public String getEmail() { return email; }

    public String getNamePrefix() { return namePrefix; }
    public String getNameSuffix() { return nameSuffix; }


    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setEmail(String email) { this.email = email; }

    public void setNamePrefix(String namePrefix) { this.namePrefix = namePrefix; }
    public void setNameSuffix(String nameSuffix) { this.nameSuffix = nameSuffix; }

    public String getFullName() {
        return firstName + " " + middleName + " " + lastName;
    }

    public boolean isDuplicatePerson(Person other) {
        if (this.getFirstName() != other.getFirstName()) return false;
        if (this.getMiddleName() != other.getMiddleName()) return false;
        if (this.getLastName() != other.getLastName()) return false;
        if (this.getEmail() != other.getEmail()) return false;
        if (this.getNamePrefix() != other.getNamePrefix()) return false;
        if (this.getNameSuffix() != other.getNameSuffix()) return false;
        return true;
    }

    // ToString method
    @Override
    public String toString() {
        return namePrefix + " " + firstName + " " + middleName + " " + lastName + " " + nameSuffix + 
               "| Email: " + email;
    }
}