<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Checkout payment page</title>
        <script>
            function SendResponse() {
                let message = {};
                //Calls the parent page function and passes the required SOP data from the iframe to the parent.
                if (document.getElementById("decision") !== null && document.getElementById("decision").value.trim() ===
                    "ACCEPT") {
                    message.tokenization = true;
                    message.decision = document.getElementById("decision").value.trim() || '';
                    message.message = document.getElementById("message").value.trim() || '';
                    message.subscriptionId = document.getElementById("payment_token").value.trim() || '';
                    message.referenceNumber = document.getElementById("req_reference_number").value.trim() || '';
                    message.verified = document.getElementById("verified") !== null ? true : false;
                    message.amount = document.getElementById("req_amount").value.trim() || '';
                    message.cardNumber = document.getElementById("req_card_number").value.trim() || '';
                    message.cardType = document.getElementById("req_card_type").value.trim() || '';
                    message.cardType = fetchCardName(message.cardType);
                    message.expiry = document.getElementById("req_card_expiry_date").value.trim() || '';
                    message.firstName = document.getElementById("req_bill_to_forename").value.trim() || '';
                    message.lastName = document.getElementById("req_bill_to_surname").value.trim() || '';
                    message.cardHolderName = document.getElementById("req_bill_to_forename").value.trim() + " " +
                        document.getElementById("req_bill_to_surname").value.trim() || '';
                    message.paymentToken = document.getElementById("request_token").value.trim() || '';
                    message.reasonCode = document.getElementById("reason_code").value.trim() || '';
                    message.transactionType = document.getElementById("req_transaction_type").value.trim() || '';
                    message.paymentMethod = document.getElementById("paymentMethod").value.trim() || '';

                } else {
                    message.error = 'error';
                    message.decision = document.getElementById("decision").value.trim() || '';
                    message.message = document.getElementById("message").value.trim() || '';
                    message.paymentMethod = document.getElementById("paymentMethod").value.trim() || '';
                    console.log('error from cybersource B2C', message);
                }
                // window.parent.onDataReceivedForTokenization(message);
                window.parent.postMessage(message, '*');
            }

            function fetchCardName(code) {
                let cardName = "UNKNOWN";
                if (code === '001') {
                    cardName = 'Visa';
                } else if (code === '002') {
                    cardName = 'Master';
                } else if (code === '003') {
                    cardName = 'AMEX';
                } else if (code === '004') {
                    cardName = 'Discover';
                } else if (code === '005') {
                    cardName = 'Diners';
                } else if (code === '007') {
                    cardName = 'JCB';
                } else if (code === '024') {
                    cardName = 'Maestro';
                }
                return cardName;
            }
        </script>
    </head>

    <body onLoad="SendResponse();">
        <form id="receipt" action="">
            <span>utf8</span>
            <input id="utf8" type="text" name="utf8" size="50" value="True" readonly/>
            <br/>
            <span>req_card_number</span>
            <input id="req_card_number" type="text" name="req_card_number" size="50" value="${paymentForm.req_card_number}" readonly/>
            <br/>
            <span>req_locale</span>
            <input id="req_locale" type="text" name="req_locale" size="50" value="${paymentForm.req_locale}" readonly/>
            <br/>
            <span>signature</span>
            <input id="signature" type="text" name="signature" size="50" value="${paymentForm.signature}" readonly/>
            <br/>
            <span>payment_token</span>
            <input id="payment_token" type="text" name="payment_token" size="50" value="${paymentForm.payment_token}" readonly/>
            <br/>
            <span>req_bill_to_surname</span>
            <input id="req_bill_to_surname" type="text" name="req_bill_to_surname" size="50" value="${paymentForm.req_bill_to_surname}"
                readonly/>
            <br/>
            <span>req_bill_to_address_city</span>
            <input id="req_bill_to_address_city" type="text" name="req_bill_to_address_city" size="50" value="${paymentForm.req_bill_to_address_city}"
                readonly/>
            <br/>
            <span>req_card_expiry_date</span>
            <input id="req_card_expiry_date" type="text" name="req_card_expiry_date" size="50" value="${paymentForm.req_card_expiry_date}"
                readonly/>
            <br/>
            <span>req_bill_to_address_postal_code</span>
            <input id="req_bill_to_address_postal_code" type="text" name="req_bill_to_address_postal_code" size="50" value="${paymentForm.req_bill_to_address_postal_code}"
                readonly/>
            <br/>
            <span>req_bill_to_phone</span>
            <input id="req_bill_to_phone" type="text" name="req_bill_to_phone" size="50" value="${paymentForm.req_bill_to_phone}" readonly/>
            <br/>
            <span>reason_code</span>
            <input id="reason_code" type="text" name="reason_code" size="50" value="${paymentForm.reason_code}" readonly/>
            <br/>
            <span>req_bill_to_forename</span>
            <input id="req_bill_to_forename" type="text" name="req_bill_to_forename" size="50" value="${paymentForm.req_bill_to_forename}"
                readonly/>
            <br/>
            <span>req_payment_method</span>
            <input id="req_payment_method" type="text" name="req_payment_method" size="50" value="${paymentForm.req_payment_method}"
                readonly/>
            <br/>
            <span>request_token</span>
            <input id="request_token" type="text" name="request_token" size="50" value="${paymentForm.request_token}" readonly/>
            <br/>
            <span>req_amount</span>
            <input id="req_amount" type="text" name="req_amount" size="50" value="${paymentForm.req_amount}" readonly/>
            <br/>
            <span>req_bill_to_email</span>
            <input id="req_bill_to_email" type="text" name="req_bill_to_email" size="50" value="${paymentForm.req_bill_to_email}" readonly/>
            <br/>
            <span>transaction_id</span>
            <input id="transaction_id" type="text" name="transaction_id" size="50" value="${paymentForm.transaction_id}" readonly/>
            <br/>
            <span>req_currency</span>
            <input id="req_currency" type="text" name="req_currency" size="50" value="${paymentForm.req_currency}" readonly/>
            <br/>
            <span>req_card_type</span>
            <input id="req_card_type" type="text" name="req_card_type" size="50" value="${paymentForm.req_card_type}" readonly/>
            <br/>
            <span>decision</span>
            <input id="decision" type="text" name="decision" size="50" value="${paymentForm.decision}" readonly/>
            <br/>
            <span>message</span>
            <input id="message" type="text" name="message" size="50" value="${paymentForm.message}" readonly/>
            <br/>
            <span>signed_field_names</span>
            <input id="signed_field_names" type="text" name="signed_field_names" size="50" value="${paymentForm.signed_field_names}"
                readonly/>
            <br/>
            <span>req_transaction_uuid</span>
            <input id="req_transaction_uuid" type="text" name="req_transaction_uuid" size="50" value="${paymentForm.req_transaction_uuid}"
                readonly/>
            <br/>
            <span>req_bill_to_address_country</span>
            <input id="req_bill_to_address_country" type="text" name="req_bill_to_address_country" size="50" value="${paymentForm.req_bill_to_address_country}"
                readonly/>
            <br/>
            <span>req_transaction_type</span>
            <input id="req_transaction_type" type="text" name="req_transaction_type" size="50" value="${paymentForm.req_transaction_type}"
                readonly/>
            <br/>
            <span>req_access_key</span>
            <input id="req_access_key" type="text" name="req_access_key" size="50" value="${paymentForm.req_access_key}" readonly/>
            <br/>
            <span>req_profile_id</span>
            <input id="req_profile_id" type="text" name="req_profile_id" size="50" value="${paymentForm.req_profile_id}" readonly/>
            <br/>
            <span>req_reference_number</span>
            <input id="req_reference_number" type="text" name="req_reference_number" size="50" value="${paymentForm.req_reference_number}"
                readonly/>
            <br/>
            <span>req_bill_to_address_state</span>
            <input id="req_bill_to_address_state" type="text" name="req_bill_to_address_state" size="50" value="${paymentForm.req_bill_to_address_state}"
                readonly/>
            <br/>
            <span>signed_date_time</span>
            <input id="signed_date_time" type="text" name="signed_date_time" size="50" value="${paymentForm.signed_date_time}" readonly/>
            <br/>
            <span>req_bill_to_address_line1</span>
            <input id="req_bill_to_address_line1" type="text" name="req_bill_to_address_line1" size="50" value="${paymentForm.req_bill_to_address_line1}"
                readonly/>
            <br/>
            <span>Signature Verified:</span>
            <input id="verified" type="text" name="verified" size="50" value="True" readonly/>
            <br/>
            <span>PaymentMethod:</span>
            <input id="paymentMethod" type="text" name="paymentMethod" size="50" value="${paymentForm.paymentMethod}" readonly/>
            <br/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
    </body>

    </html>