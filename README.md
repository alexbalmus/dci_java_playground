# A DCI-inspired Approach For Java

Note: if you arrived at this page after following the discussion on https://groups.google.com/g/object-composition/c/YM0UNIIx_b8 
then it's important to know that the wrapper approach previously found here was moved (and improved) to this other project: https://github.com/alexbalmus/euw 

This page describes the "reverse-wrapper" approach. Read on to find out.

Other approaches I've tried:
- The "method naming" approach: https://github.com/alexbalmus/dci_java_playground/tree/method_naming_approach
- The "method reference" approach: https://github.com/alexbalmus/dci_java_playground/tree/method_reference_approach
- "Entity - UseCase - Wrapper": https://github.com/alexbalmus/euw


If you are new to Data-Context-Interaction, then it's recommended you read the following article first:
https://fulloo.info/Documents/ArtimaDCI.html

Please note that given Java's dynamic limitations and the considerations mentioned below, this is not true DCI.

DCI is a valuable (but not very well known) use case oriented design & architecture approach 
and OOP paradigm shift. Due to its particular characteristics, it's rather difficult to implement in a strongly typed 
programming language like Java. Two reference examples have been provided by DCI's authors, one using a library called 
Qi4J and the other using Java's reflection API: https://fulloo.info/Examples/JavaExamples/ 

In my case, I'm going for some tradeoffs: this is not "pure" DCI, but still aiming to be as close as possible to 
the valuable features DCI brings.

Prior considerations:
- Pure Java for roles/role-injection - no third party libraries / frameworks, as they might not be accepted in certain projects
- No reflection - this also might not be accepted in some projects, and Java's reflection API is a pain to work with

Approach taken for roles: "reverse-wrapper" role object injected into objects that accept it, thus making new (contextual) methods available.

Provided interfaces:

- Role: implemented by "reverse-wrapper" role objects:

com.alexbalmus.reversewrapper.common.Role:

```java
    /**
    * Basic role interface
    * @param <E> the generic type of the entity which will play the role
    */
    public interface Role<E>
    {
        /**
         * @return a reference to the role-playing entity
         */
        E thiz();
    }
```

- RolePlayer: implemented by "role-playing" objects:

com.alexbalmus.reversewrapper.common.RolePlayer:

```java
    /**
     * Interface to be implemented by role-playing objects
     */
    public interface RolePlayer
    {
        /**
         * Accept a role during use-case execution
         * @param role the role to be played
         */
        void acceptRole(Role<? extends RolePlayer> role);
    
        /**
         * Expose the role object to invoke the role methods
         * @return the role object
         * @param <R> the generic type of the role object to cast to
         */
        <R extends Role<? extends RolePlayer>> R role();

        /**
         * Clear the role object at the end of use-case execution
         */   
        default void clearRole()
        {
            acceptRole(null);
        }
    }
```

Provided abstract class for JPA entities:

com.alexbalmus.reversewrapper.common.jpa.AbstractRolePlayingJpaEntity:

```java
    /**
     * Abstract class implementing {@link com.alexbalmus.reversewrapper.common.RolePlayer}
     * for JPA entities
     */
    @MappedSuperclass
    public class AbstractRolePlayingJpaEntity implements RolePlayer 
    {
        @Transient // This field is not persisted in the database
        Role<? extends RolePlayer> role;
        
        @Override
        public void acceptRole(Role<? extends RolePlayer> role) 
        {
            if (role != null && role.thiz() != this)
            {
                throw new IllegalArgumentException("Invalid role.");
            }
            
           this.role = role;
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public <R extends Role<? extends RolePlayer>> R role() 
        {
            if (this.role == null)
            {
                throw new IllegalStateException("No role has been assigned to this entity.");
            }
            
           try
           {
               return (R) this.role;
           }
           catch (ClassCastException e)
           {
               throw new IllegalStateException("Attempting to play an invalid role.");
           }
        }
    }
```

Custom annotation for DCI contexts as a Spring @Component stereotype: com.alexbalmus.reversewrapper.common.DciContext

An actual role method might look something like this:

com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer.Account_Source:

```java
    /**
     * Source account role
     */
    interface Account_Source extends Role<Account> 
    {
        String INSUFFICIENT_FUNDS = "Insufficient funds.";
    
        default void transfer(final Double amount, final Account destination) 
        {
            if (!(destination.role() instanceof Account_Destination)) 
            {
                throw new IllegalArgumentException("Not a destination account.");
            }
    
            if (thiz().getBalance() < amount) 
            {
                throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
            }
    
            thiz().decreaseBalanceBy(amount);
    
            destination.<Account_Destination>role().receive(amount);
        }
    }
```

The context would select the objects participating in the use case and call the necessary role methods:

com.alexbalmus.reversewrapper.examples.bankaccounts.dcicontexts.moneytransfer.MoneyTransferDciContext.transferFromSourceToDestination:

```java
    public void transferFromSourceToDestination(
        final Account source, final Account destination, final Double amount)
    {
        //--- Use case roles setup:
        source.acceptRole(createSourceRole(source));
        destination.acceptRole(createDestinationRole(destination));

        //--- Interaction:
        source.<Account_Source>role().transfer(amount, destination);

        //--- Clear roles:
        source.clearRole();
        destination.clearRole();
    }
```


More info:

https://fulloo.info/ 

https://fulloo.info/Documents/ArtimaDCI.html

https://en.wikipedia.org/wiki/Data,_context_and_interaction

https://blog.encodeart.dev/series/dci-typescript-tutorial
