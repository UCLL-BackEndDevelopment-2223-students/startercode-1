package demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    // given
    private String validNameElke = "Elke";
    private int validAgeElke = 45;
    private String validEmailElke = "elke.steegmans@ucll.be";
    private String validPasswordElke = "ikgahetnietvertellenhoor999";

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    // constructor
    // happy case
    @Test
    void givenValidValues_whenCreatingUser_thenUserIsCreatedWithThoseValues() {
        // when
        User elke = new User(validNameElke, validAgeElke, validEmailElke, validPasswordElke);

        // then
        assertNotNull(elke);
        assertEquals(validNameElke, elke.getName());
        assertEquals(validAgeElke, elke.getAge());
        assertEquals(0, elke.countYearsOfMembership());
        assertEquals(validEmailElke, elke.getEmail());
        assertEquals(validPasswordElke, elke.getPassword());
        Set<ConstraintViolation<User>> violations = validator.validate(elke);
        assertTrue(violations.isEmpty());
    }

    // constructor
    // unhappy case
    // invalid negative age
    @Test
    void givenInvalidNegativeAge_whenCreatingUser_thenAgeViolationMessageIsThrown() {
        // when
        User eric = new User("Eric", -5, "eric@ucll.be", "elke1eric2");

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(eric);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("age may not be negative", violation.getMessage());
    }

    // constructor
    // unhappy case
    // invalid empty name (" ")
    @Test
    void givenInvalidEmptyName_whenCreatingUser_thenNameViolationMessageIsThrown() {
        // when
        User eric = new User("    ", 65, "eric@ucll.be", "elke1eric2");

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(eric);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("name may not be empty", violation.getMessage());
    }

    // constructor
    // unhappy case
    // invalid empty name ("")
    @Test
    void givenInvalidNoName_whenCreatingUser_thenNameViolationMessageIsThrown() {
        // when
        User eric = new User("", 65, "eric@ucll.be", "elke1eric2");

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(eric);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("name may not be empty", violation.getMessage());
    }

    // constructor
    // unhappy case
    // invalid email (no @)
    @Test
    void givenInvalidEmailNoAt_whenCreatingUser_thenUserIsCreatedWithEmailNull() {
        // when
        User elke = new User(validNameElke, validAgeElke, "elke.steegmans.ucll.be", validPasswordElke);

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(elke);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("no valid email", violation.getMessage());
    }

    /**
     * constructor
     * unhappy case
     * invalid password (white spaces)
     * TIP: have a look at
     * - https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-
     * expressions-in-java/
     * - https://www.tabnine.com/code/java/classes/org.hibernate.validator.Pattern
     **/
    @Test
    void givenInvalidPasswordWhiteSpaces_whenCreatingUser_thenUserIsCreatedWithDefaultPasswordt() {
        // when
        User elke = new User(validNameElke, validAgeElke, validEmailElke, "    1lke   ");

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(elke);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("password must be minimum 8 characters and may not contain white spaces", violation.getMessage());
    }

    /**
     * constructor
     * unhappy case
     * invalid password (white spaces)
     * TIP: have a look at
     * - https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-
     * expressions-in-java/
     * - https://www.tabnine.com/code/java/classes/org.hibernate.validator.Pattern
     **/
    @Test
    void givenInvalidPasswordNoDigit_whenCreatingUser_thenUserIsCreatedWithDefaultPasswordt() {
        // when
        User elke = new User(validNameElke, validAgeElke, validEmailElke, "elkeelke");

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(elke);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("password must contain a digit", violation.getMessage());
    }

    /**
     * constructor
     * unhappy case
     * invalid password (white spaces)
     * TIP: have a look at
     * - https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-
     * expressions-in-java/
     * - https://www.tabnine.com/code/java/classes/org.hibernate.validator.Pattern
     **/
    @Test
    void givenInvalidPasswordNotMin8Chars_whenCreatingUser_thenUserIsCreatedWithDefaultPasswordt() {
        // when
        User elke = new User(validNameElke, validAgeElke, validEmailElke, "el2elke");

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(elke);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("password must be minimum 8 characters and may not contain white spaces", violation.getMessage());
    }

    /**
     * constructor
     * unhappy case
     * invalid password (white spaces)
     * TIP: have a look at
     * - https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-
     * expressions-in-java/
     * - https://www.tabnine.com/code/java/classes/org.hibernate.validator.Pattern
     **/
    @Test
    void givenInvalidPasswordNotMin8CharsAndNoDigit_whenCreatingUser_thenUserIsCreatedWithDefaultPasswordt() {
        // when
        User elke = new User(validNameElke, validAgeElke, validEmailElke, "elelke");

        // then
        Set<ConstraintViolation<User>> violations = validator.validate(elke);
        assertEquals(2, violations.size());
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<User> violation : violations) {
            errors.add(violation.getMessage());
        }
        assertTrue(errors.contains("password must contain a digit"));
        assertTrue(errors.contains("password must be minimum 8 characters and may not contain white spaces"));
    }

    // toString
    // happy case
    @Test
    void givenValidUser_whenToString_thenUserObjectisReturnedInStringRepresentation() {
        // given
        User elke = new User(validNameElke, validAgeElke, validEmailElke, validPasswordElke);

        // when
        String result = elke.toString();

        // then
        assertEquals("Elke is 45 years old and has as email elke.steegmans@ucll.be", result);
    }

}