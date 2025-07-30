# A DCI-inspired Approach For Java

Inspired from Andreas Söderlund's DCI tutorial for TypeScript: https://blog.encodeart.dev/series/dci-typescript-tutorial

Please note that given Java's dynamic limitations and the considerations mentioned below, this is not true DCI.

Also, checkout another approach I've tried, which I call "Entity - UseCase - Wrapper": https://github.com/alexbalmus/euw

If you are new to Data-Context-Interaction, then it's recommended you read the following article first:
https://fulloo.info/Documents/ArtimaDCI.html

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

Approach taken for role methods in Java: variables of type functional interface with a naming convention.

We start by defining a custom functional interface that represents a role method:

com.alexbalmus.dcibankaccounts.common.RoleMethod:

```java
    /**
     * Represents a DCI role method that contributes new contextual behavior to an object
     * @param <T> method argument
     */
    public interface RoleMethod<T>
    {
        void call(T t);
    }
```

An actual role method might look something like this:

com/alexbalmus/dcibankaccounts/usecases/moneytransfer/MoneyTransferService.java:

```java
    // Source account:
    RoleMethod<Double> source_transferToDestination = (amount) ->
    {
        if (source.getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        source.decreaseBalanceBy(amount);

        // equivalent of: destination.receive(amount):
        destination_receive.call(amount);
    };
```

When calling this, i.e. source_transferToDestination.call(amount), it emulates the equivalent of: source.transferToDestination(amount)

The context would select the objects participating in the use case and call the necessary role methods:

com.alexbalmus.dcibankaccounts.usecases.moneytransfer.MoneyTransferService.executeSourceToDestinationTransfer:

```java
    /**
     * DCI context (use case): transfer amount from source account to destination account
     */
    public void executeSourceToDestinationTransfer(
        final Double amountToTransfer,
        final A source,
        final A destination)
    {
        //----- Role methods:

        // Destination account:
        RoleMethod<Double> destination_receive = (amount) ->
        {
            destination.increaseBalanceBy(amount);
        };

        // Source account:
        RoleMethod<Double> source_transferToDestination = (amount) ->
        {
            if (source.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            source.decreaseBalanceBy(amount);

            // equivalent of: destination.receive(amount):
            destination_receive.call(amount);
        };


        //----- Interaction:

        // equivalent of: source.transferToDestination(amount)
        source_transferToDestination.call(amountToTransfer);
    }
```

More info:

https://fulloo.info/ 

https://fulloo.info/Documents/ArtimaDCI.html

https://en.wikipedia.org/wiki/Data,_context_and_interaction

https://blog.encodeart.dev/series/dci-typescript-tutorial
