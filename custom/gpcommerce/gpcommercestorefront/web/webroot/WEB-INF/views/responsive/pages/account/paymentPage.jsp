
<div class="vx-add-payment-form">
  <h1>vx-add-payment-form Component </h1>
 
  <form id="credit-card-form" action="" method="post">
    <!-- <input type="text" name="transaction_type" value="create_payment_token" />
    <input type="text" name="signed_field_names" value="access_key,amount,bill_to_address_city,bill_to_address_country,bill_to_address_line1,bill_to_address_postal_code,bill_to_address_state,bill_to_email,bill_to_forename,bill_to_phone,bill_to_surname,currency,locale,payment_method,profile_id,reference_number,signed_date_time,signed_field_names,transaction_type,transaction_uuid,unsigned_field_names"
    />
    <input type="text" name="unsigned_field_names" value="card_cvn,card_expiry_date,card_number,card_type" /> -->
 
    <div class="form-elements">
      <div class="row">
        <div class="col-sm-6">
          <div>
            Card Number
          </div>
          <div>
            <input type="text" class="input-elements" name="card_number" v-model="paymentform.cardnumber" value="4111111111111111" />
          </div>
        </div>
        <div class="col-sm-6 right-side-content">
          <div>
            Card Holder Name
          </div>
        </div>
        <div>
          <input type="text" class="input-elements" name="card_holder_name" v-model="paymentform.cardholdername" value="John Doe" />
        </div>
      </div>
    </div>
 
    <div class="form-elements">
      <div class="row">
        <div class="col-sm-8">
          <div>
            Expiry
          </div>
          <div>
            <div class="card-expiry-block">
              <input type="hidden" name="card_expiry_date" />
              <div class="month-select-box">
              	<input type="text" class="input-elements" name="card_number" v-model="paymentform.month" value="11" />
                
              </div>
              <div class="year-select-box">
                <input type="text" class="input-elements" name="card_number" v-model="paymentform.year" value="2020" />
              </div>
            </div>
          </div>
        </div>
        <div class="col-sm-4 right-side-content">
          <div>
            CVV
          </div>
        </div>
        <div>
          <input type="text" class="input-elements" name="card_cvn" v-model="paymentform.cardcvn" />
        </div>
      </div>
    </div>
 
    <div class="form-elements">
      <div class="row">
        <div class="col-sm-12">
          <div class="top-block">
            Billing Address
          </div>
          <div class="bottom-block">
            <div>
              <input type="checkbox" class="form-check-input" :label="radioitem.label" :value="radioitem.value" :name="radiobuttonname"
                v-model="paymentform.shippingaddress" />
              <label :for="radiobuttonname" class="form-check-label">use shipping address</label>
            </div>
          </div>
        </div>
      </div>
    </div>






    <div class="form-elements">
      <div class="address-select-box">
        Address
        <input type="text" class="input-elements" name="card_number" v-model="paymentform.address" />
      </div>
    </div>
    <div class="form-elements">
      <div>
        Country
      </div>
      <div class="country-select-box">
        <input type="text" class="input-elements" name="card_number" v-model="paymentform.country" />
      </div>
    </div>
    <div class="form-elements">
      <div>
        Billing Address 1
      </div>
      <div>
        <input type="text" class="input-elements" name="bill_to_address_line1" />
      </div>
    </div>
    <div class="form-elements">
      <div>
        Billing Address 2
      </div>
      <div>
        <input type="text" class="input-elements" name="bill_to_address_line2" />
      </div>
    </div>
    <div class="form-elements">
      <div>
        City
      </div>
      <div>
        <input type="text" class="input-elements" name="bill_to_address_city" />
      </div>
    </div>
    <div class="form-elements">
      <div>
        State
      </div>
      <div class="state-select-box">
        <input type="text" class="input-elements" name="card_number" v-model="paymentform.state" />
      </div>
    </div>
    <div class="form-elements">
      <div>
        Zip/Postal Code
      </div>
      <div>
        <input type="text" class="input-elements" name="bill_to_address_postal_code" />
      </div>
    </div>
 
    <div class="form-elements">
      <div>
        Email Address
      </div>
      <div>
        <input type="email" class="input-elements" name="bill_to_email" />
      </div>
    </div>
    <div class="form-elements">
      <div>
        Phone Number
      </div>
      <div>
        <input type="tel" class="input-elements" name="bill_to_phone" />
      </div>
    </div> 
 
    <input id="payment-click" type="button" name="submit" value="submit" />
    
  </form>
 
  <iframe id="destination" class="payment-form-iframe" name="paymentformiframe" src="" height="540" width="690">
 
  </iframe>
 
</div>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.js"></script>
<script type="text/javascript">
	console.log();
	$(document).ready(function(){
		$("#payment-click").on("click",function(){
			//passFormValuestoIframe(this);
			
			$("#destination").attr("src","https://localhost:9002/gpcommercestorefront/_ui/responsive/common/templates/credit-card-form.html");
		})		
	});	  
</script>
