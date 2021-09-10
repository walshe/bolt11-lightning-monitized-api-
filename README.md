# bolt11-lightning-monitized-api


This app allows an admin user to configure some of the apis in the apps as MonetizedApi's and specifying a price in sats to invoke them.

Non admin users must have a sufficient balance in sats to access such apis.

A user registers a balance by using the app to generate a Bolt 11 invoice that they can then pay outside of the app. 

If the invoice has been paid then this will be resolved by the app and the users balance will be credited.

The user can now access any apis that have been monetized by the admin and their balance is decremented by the appropriate number of sats after every invcation.

If a user does not have a sifficent balance to invoke an api then a 402 Http code is returned in the response: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/402

