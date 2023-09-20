# DCI Java Playground
Experimenting with the DCI (Data-Context-Interaction) paradigm in Java.

If you are new to DCI, then it's recommended you read the following article first:
https://fulloo.info/Documents/ArtimaDCI.html

Approach taken for roles in Java: interfaces with default methods 
(one of the suggestions from the Wikipedia article listed in the More info section). 

We start out with a generic SAM (Single Abstract Method) interface called Role having a method self() 
which would return a reference to the target (role playing) object (the equivalent of "this"):

    public interface Role<T>
    {
        T self();
    }

https://github.com/alexbalmus/dci_java_playground/blob/main/src/main/java/com/alexbalmus/dcibankaccounts/usecases/Role.java

An actual role might look something like this:

    interface Account_SourceRole extends Role<Account>
    {
        String INSUFFICIENT_FUNDS = "Insufficient funds.";
    
        default void transfer(Double amount, Account_DestinationRole destination)
        {
            // Begin transaction.
            if (self().getBalance() < amount)
            {
                throw new RuntimeException(INSUFFICIENT_FUNDS); // Rollback.
            }
            self().decreaseBalanceBy(amount);
            destination.receive(amount);
            // End transaction.
        }
    }

https://github.com/alexbalmus/dci_java_playground/blob/main/src/main/java/com/alexbalmus/dcibankaccounts/usecases/moneytransfer/Account_SourceRole.java

Now, for the actual "role injection" that will be done inside a context, this will be simulated by means of an 
anonymous inner class that implements a role interface and who's instance will wrap the target object; 
the implementation of the self() method will return the wrapped target object.

Starting with Java 8 this can be done very elegandly behind the scenes using a lambda expression:

    Account_SourceRole assignSourceRoleTo(final Account source)
    {
        return () -> source;
    }

The old-school (less pleasant) alternative to the above would look like:

    Account_SourceRole assignSourceRoleTo(final Account source)
    {
        return new Account_SourceRole()
        {
            @Override
            public Account self()
            {
                return source;
            }
        };
    }

The context object would select the objects participating in the use case, assign the necessary roles to them 
and then kick-off the use case:

    ...

    this.sourceAccount = assignSourceRoleTo(accountsRepository.findById(sourceId));
    this.destinationAccount = assignDestinationRoleTo(accountsRepository.findById(destinationId));
    
    ...

    public void execute()
    {
        sourceAccount.transfer(amount, destinationAccount);
    }

https://github.com/alexbalmus/dci_java_playground/blob/main/src/main/java/com/alexbalmus/dcibankaccounts/usecases/moneytransfer/MoneyTransferContext.java

Assigning multiple roles could be approached by defining a new role interface that extends multiple role interfaces:
    
    public interface Account_SourceAndDestinationRole
        extends Account_SourceRole, Account_DestinationRole
    {
    }

Inheritance is usually discouraged in DCI, but here I'm aiming for a minimal use - just to combine multiple roles.

https://github.com/alexbalmus/dci_java_playground/blob/main/src/main/java/com/alexbalmus/dcibankaccounts/usecases/moneytransfer/Account_SourceAndDestinationRole.java


I've also attempted an example of nested contexts:

https://github.com/alexbalmus/dci_java_playground/blob/main/src/main/java/com/alexbalmus/dcibankaccounts/usecases/moneytransfer/ABCMoneyTransferContext.java




More info:

https://fulloo.info/ 

https://www.artima.com/articles/the-dci-architecture-a-new-vision-of-object-oriented-programming 

https://en.wikipedia.org/wiki/Data,_context_and_interaction

https://gist.github.com/kt3k/8312661
