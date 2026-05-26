# An attempt to implement a DCI example in Java

If you are new to Data-Context-Interaction, then it's recommended you read the following article first:
https://fulloo.info/Documents/ArtimaDCI.html

Running the example:
- tested with JDK 25 & Maven 3.9.15
- building: mvn package
- running: mvn spring-boot:run

DCI is a valuable (but not very well known) software design & architecture approach and OOP paradigm shift. 

Some key ideas:
- two orthogonal perspectives: "what the system is" (Data) and "what the system does" (Interaction in a Context)
- explicitly capture object interaction as a network of roles played by objects in a context
- objects should initially be simple / "dumb" / data-objects: they may contain methods related to themselves (for
validations, invariants protection etc.) but not for interaction with other objects
- objects will receive additional behavior from the roles they play in a certain context
- identity of the role-playing objects must be retained (i.e. a role may not be implemented as a wrapper)

Due to the particular characteristics, it's rather difficult to implement in a strongly typed 
programming language like Java. Two reference examples have been provided by DCI's authors, one using a library called 
Qi4J and the other using Java's reflection API: https://fulloo.info/Examples/JavaExamples/

Approach taken here: extension methods provided by Project Lombok: https://projectlombok.org/features/experimental/ExtensionMethod 

In this example, "Data" is represented by simple JPA entities of type Account:

[Account](https://github.com/alexbalmus/dci_java_playground/blob/ext_method_lombok_approach/src/main/java/com/alexbalmus/javadci/examples/bankaccounts/entities/Account.java):

```java
@Entity
@Table(name="account")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "balance")
    private Double balance;

    // Constructors, getters and setters omitted for brevity.

    public void increaseBalanceBy(final Double amount)
    {
        balance += amount;
    }

    public void decreaseBalanceBy(final Double amount)
    {
        if (balance < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS);
        }
        balance -= amount;
    }
    
    // equals and hashCode omitted for brevity.
}
```

The following are two roles used in a money transfer scenario: the "source account" role and the "destination account" role:

[MoneyTransferContext.Account_Source](https://github.com/alexbalmus/dci_java_playground/blob/ext_method_lombok_approach/src/main/java/com/alexbalmus/javadci/examples/bankaccounts/usecases/moneytransfer/MoneyTransferContext.java#L48):

```java
    /**
     * Account_Source role
     * Uses extension method MoneyTransferContext.Account_Destination#receive
     */
    @DciRole
    @ExtensionMethod(MoneyTransferContext.Account_Destination.class)
    static class Account_Source
    {
        @SuppressWarnings("unused")
        public static void transfer(Account thiz, Account destination, Double amount)
        {
            thiz.decreaseBalanceBy(amount);
            destination.receive(amount);
        }
    }
```
The Lombok annotation @ExtensionMethod specifies that this class will use the extension method defined in Account_Destination.

The custom annotation @DciRole is a marker to better identify DCI roles.

Notice how "destination" gains the new (contextual) extension method called "receive", which is defined below:

[MoneyTransferContext.Account_Destination](https://github.com/alexbalmus/dci_java_playground/blob/ext_method_lombok_approach/src/main/java/com/alexbalmus/javadci/examples/bankaccounts/usecases/moneytransfer/MoneyTransferContext.java#L62):

```java
    /**
     * Account_Destination role
     */
    @DciRole
    static class Account_Destination
    {
        @SuppressWarnings("unused")
        public static void receive(Account thiz, Double amount)
        {
            thiz.increaseBalanceBy(amount);
        }
    }
```

The context gathers the objects participating in the use case and calls the necessary role methods:

[MoneyTransferContext](https://github.com/alexbalmus/dci_java_playground/blob/ext_method_lombok_approach/src/main/java/com/alexbalmus/javadci/examples/bankaccounts/usecases/moneytransfer/MoneyTransferContext.java#L20):

```java
/**
 * Money transfer DCI context.
 * Uses extension method MoneyTransferContext.Account_Source#transfer
 */
@DciContext
@ExtensionMethod(MoneyTransferContext.Account_Source.class)
public class MoneyTransferContext
{
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds.";

    /**
     * DCI context (use case): transfer amount from source account to destination account
     */
    public void executeSourceToDestinationTransfer(
        final Double amountToTransfer,
        final Account source,
        final Account destination)
    {
        source.transfer(destination, amountToTransfer);
    }
    
    // Other code omitted for brevity.
}
```

The Lombok annotation @ExtensionMethod specifies that this class will use the extension method defined in Account_Source.

The custom annotation @DciContext is a marker to better identify DCI Contexts. For convenience, it's also shaped as a custom stereotype for Spring's @Component.

Notice how "source" gains the new (contextual) extension method called "transfer".


Other approaches I've tried:

Non-DCI, but still aiming to capture some of the valuable ideas from it:

- ["Entity - UseCase - Wrapper"](https://github.com/alexbalmus/euw)

Other DCI candidates (not necessarily compliant):

- [The "method reference" approach](https://github.com/alexbalmus/dci_java_playground/tree/method_reference_approach)

- [The "method naming" approach](https://github.com/alexbalmus/dci_java_playground/tree/method_naming_approach)

- [The "reverse wrapper" approach](https://github.com/alexbalmus/dci_java_playground/tree/reverse_wrapper_approach)


References:

https://fulloo.info/ 

https://fulloo.info/Documents/ArtimaDCI.html

https://en.wikipedia.org/wiki/Data,_context_and_interaction

https://blog.encodeart.dev/series/dci-typescript-tutorial
