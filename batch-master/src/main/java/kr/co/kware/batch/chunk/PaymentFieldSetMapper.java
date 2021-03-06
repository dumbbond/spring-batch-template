package kr.co.kware.batch.chunk;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * @author Marius Bogoevici
 * @author Gunnar Hillert
 */
public class PaymentFieldSetMapper implements FieldSetMapper<Payment> {

    @Override
    public Payment mapFieldSet(FieldSet fieldSet) throws BindException {

        final Payment payment = new Payment();

        payment.setSourceAccountNo(fieldSet.readString("source"));
        payment.setDestinationAccountNo(fieldSet.readString("destination"));
        payment.setAmount(fieldSet.readBigDecimal("amount"));
        payment.setDate(fieldSet.readDate("date"));

        return payment;
    }
}
