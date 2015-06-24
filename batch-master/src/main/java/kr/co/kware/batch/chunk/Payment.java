package kr.co.kware.batch.chunk;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Marius Bogoevici
 * @author Gunnar Hillert
 */
public class Payment {

    private String sourceAccountNo;

    private String destinationAccountNo;

    private BigDecimal amount;

    private Date date;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getDateFormatted() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public String getDestinationAccountNo() {
        return destinationAccountNo;
    }

    public void setDestinationAccountNo(String destinationAccountNo) {
        this.destinationAccountNo = destinationAccountNo;
    }

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
    }

    @Override
    public String toString() {
        return "Payment [sourceAcct#=" + sourceAccountNo
                + ", destAcct#=" + destinationAccountNo
                + ", amount=" + amount + ", date=" + getDateFormatted() + "]";
    }
}
