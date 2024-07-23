# A DCI-inspired Approach For Java
If you are new to Data-Context-Interaction, then it's recommended you read the following article first:
https://fulloo.info/Documents/ArtimaDCI.html

Please note that given Java's dynamic limitations and the considerations mentioned below, this is not true DCI.

Also, checkout another approach I've tried: https://github.com/alexbalmus/dci_java_playground/tree/wrapper_approach 

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

Approach taken for role methods in Java: variables of type functional interface Consumer (simulating functions with single argument and void return type), with a naming convention.

Inspired from https://blog.encodeart.dev/series/dci-typescript-tutorial

An actual role might look something like this:

com/alexbalmus/dcibankaccounts/usecases/moneytransfer/MoneyTransferContext.java:49:

    // Source account:
    Consumer<Double> sourceAccount_transferToDestination = (amount) ->
    {
        if (sourceAccount.getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        sourceAccount.decreaseBalanceBy(amount);

        // equivalent of: destinationAccount.receive(amount):
        destinationAccount_receive.accept(amount);
    };

The context object would select the objects participating in the use case and call the necessary role methods:

com.alexbalmus.dcibankaccounts.usecases.moneytransfer.MoneyTransferContext.executeSourceToDestinationTransfer:

    public void executeSourceToDestinationTransfer()
    {
        // Role methods:

        // Destination account:
        Consumer<Double> destinationAccount_receive = (amount) ->
        {
            destinationAccount.increaseBalanceBy(amount);
        };

        // Source account:
        Consumer<Double> sourceAccount_transferToDestination = (amount) ->
        {
            if (sourceAccount.getBalance() < amount)
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
            sourceAccount.decreaseBalanceBy(amount);

            // equivalent of: destinationAccount.receive(amount):
            destinationAccount_receive.accept(amount);
        };

        // Interaction:

        // equivalent of: sourceAccount.transferToDestination(amount)
        sourceAccount_transferToDestination.accept(amount);
    }


More info:

https://fulloo.info/ 

https://fulloo.info/Documents/ArtimaDCI.html

https://en.wikipedia.org/wiki/Data,_context_and_interaction

https://blog.encodeart.dev/series/dci-typescript-tutorial
