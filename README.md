# A DCI-inspired Approach For Java
If you are new to Data-Context-Interaction, then it's recommended you read the following article first:
https://fulloo.info/Documents/ArtimaDCI.html

Please note that given Java's dynamic limitations and the considerations mentioned below, this implementation is a "wrapper approach" and therefore is not true DCI: https://fulloo.info/doku.php?id=why_isn_t_it_dci_if_you_use_a_wrapper_object_to_represent_the_role

DCI is a valuable (but not very well known) use case oriented design & architecture approach 
and OOP paradigm shift. Due to its particular characteristics, it's rather difficult to implement in a strongly typed 
programming language like Java. Two reference examples have been provided by DCI's authors, one using a library called 
Qi4J and the other using Java's reflection API: https://fulloo.info/Examples/JavaExamples/ 

In my case, I'm going for some tradeoffs: this is not "pure" DCI, but still aiming to be as close as possible to 
the valuable features DCI brings.

Prior considerations:
- Pure Java for roles/role-injection - no third party libraries / frameworks, as they might not be accepted in certain projects
- No reflection - this also might not be accepted in some projects, and Java's reflection API is a pain to work with
- Able to integrate in a mature/legacy code base, i.e., not requiring any changes to existing entities.

Approach taken for roles in Java: interfaces with default methods 
(one of the suggestions from the Wikipedia article listed in the "More info" section below). 

We start out with a generic functional/SAM (Single Abstract Method) interface called Role having a method self() 
which would return a reference to the target (role playing) object (the equivalent of "this"):

com.alexbalmus.dcibankaccounts.usecases.Role:

    public interface Role<T>
    {
        T self();
    }

An actual role might look something like this:

com.alexbalmus.dcibankaccounts.usecases.moneytransfer.MoneyTransferContext.Account_SourceRole:

    interface Account_SourceRole<A extends Account> extends Role<A>
    {
        String INSUFFICIENT_FUNDS = "Insufficient funds.";
    
        default void transfer(final Double amount, final Account_DestinationRole<? super A> destination)
        {
            if (self().getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            self().decreaseBalanceBy(amount);
            destination.receive(amount);
        }
    }

Now, for the actual "role injection" that will be done inside a context, this will be simulated by means of an 
anonymous inner class that implements a role interface and who's instance will wrap the target object; 
the implementation of the self() method will return the wrapped target object.

Starting with Java 8 this can be done very elegantly behind the scenes using a lambda expression:

    Account_SourceRole<A> assignSourceRoleTo(final A source)
    {
        return () -> source;
    }

The old-school (less pleasant) alternative to the above would look like:

    Account_SourceRole<A> assignSourceRoleTo(final A source)
    {
        return new Account_SourceRole<>()
        {
            @Override
            public A self()
            {
                return source;
            }
        };
    }

The context object would select the objects participating in the use case, assign the necessary roles to them 
and then kick-off the use case:

    ...

    this.sourceAccount = assignSourceRoleTo(source);
    this.destinationAccount = assignDestinationRoleTo(destination);
    
    ...

com.alexbalmus.dcibankaccounts.usecases.moneytransfer.MoneyTransferContext.executeSourceToDestinationTransfer:

    public void executeSourceToDestinationTransfer()
    {
        sourceAccount.transfer(amount, destinationAccount);
    }

Assigning multiple roles could be approached by defining a new role interface that extends multiple role interfaces:

com.alexbalmus.dcibankaccounts.usecases.moneytransfer.MoneyTransferContext.Account_SourceAndDestinationRole:

    public interface Account_SourceAndDestinationRole<A extends Account>
        extends Account_SourceRole<A>, Account_DestinationRole<A>
    {
    }

Inheritance is usually discouraged in DCI, but here I'm aiming for a minimal use - just to combine multiple roles.


More info:

https://fulloo.info/ 

https://fulloo.info/Documents/ArtimaDCI.html

https://en.wikipedia.org/wiki/Data,_context_and_interaction

https://gist.github.com/kt3k/8312661
