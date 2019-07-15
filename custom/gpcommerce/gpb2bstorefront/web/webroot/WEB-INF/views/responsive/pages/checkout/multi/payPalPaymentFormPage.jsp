<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Checkout payment page</title>
        <script>
            let message = {};
            function SendResponse() {
                message.status = '${paymentForm.status}';
                message.paymentMethod = '${paymentForm.paymentType}';
                if (window.opener) {
                    window.opener.postMessage(message, '*');
                    window.close();
                }
            }
            var eventMethod = window.addEventListener
                ? 'addEventListener'
                : 'attachEvent';
            var eventer = window[eventMethod];
            var messageEvent =
            eventMethod === 'attachEvent' ? 'onmessage' : 'message';
            eventer(messageEvent, function(e) {
                if (!window.opener) {
                    e.source.postMessage(message, '*');
                    window.close();
                }
            });
        </script>
    </head>
    <body onLoad="SendResponse();"></body>
</html>