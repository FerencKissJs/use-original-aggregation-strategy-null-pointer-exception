package route;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class CustomAggregatorStrategy implements AggregationStrategy {

    private final Exchange original;
    private final boolean propagateException;

    public CustomAggregatorStrategy() {
        this(null, true);
    }

    public CustomAggregatorStrategy(boolean propagateException) {
        this(null, propagateException);
    }

    public CustomAggregatorStrategy(Exchange original, boolean propagateException) {
        this.original = original;
        this.propagateException = propagateException;
    }

    /**
     * Creates a new instance as a clone of this strategy with the new given original.
     */
    public CustomAggregatorStrategy newInstance(Exchange original) {
        return new CustomAggregatorStrategy(original, propagateException);
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        boolean exceptionOccurred = false;
        final Exchange exchangeToUpdate = (oldExchange != null) ? oldExchange : newExchange;

        if (propagateException) {
            Exception exception = checkException(oldExchange, newExchange);
            if (exception != null) {
                exceptionOccurred = true;
                if (original != null) {
                    original.setException(exception);
                } else {
                    exchangeToUpdate.setException(exception);
                }
            }
            exception = checkCaughtException(oldExchange, newExchange);
            if (exception != null) {
                exceptionOccurred = true;
                if (original != null) {
                    original.setProperty(Exchange.EXCEPTION_CAUGHT, exception);
                } else {
                    exchangeToUpdate.setProperty(Exchange.EXCEPTION_CAUGHT, exception);
                }
            }
        }
        if (exceptionOccurred) {
            return original != null ? original : exchangeToUpdate;
        }else{
            return original != null ? original : oldExchange;
        }
    }

    protected Exception checkException(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange.getException();
        } else {
            return (newExchange != null && newExchange.getException() != null)
                    ? newExchange.getException()
                    : oldExchange.getException();
        }
    }

    protected Exception checkCaughtException(Exchange oldExchange, Exchange newExchange) {
        Exception caught = newExchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        if (caught == null && oldExchange != null) {
            caught = oldExchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        }
        return caught;
    }

    public Exchange getOriginal() {
        return original;
    }

    @Override
    public String toString() {
        return "CustomAggregatorStrategy";
    }
}
