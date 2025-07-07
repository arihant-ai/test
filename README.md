# CallLoggerApp

This is a minimal Android application that demonstrates how to monitor phone
calls and send call information to a remote API. When a call ends, the app
shows a popup allowing the user to enter a case number and comment. The data is
optionally sent to a configured API endpoint. Numbers can be blocked so that
no API call is made on subsequent calls. When a blocked or known number rings,
a small overlay similar to Truecaller displays the previous comment.

Odoo connection details (URL, database, username and password) and the API URL
can be configured in **Settings**. The app uses XML‑RPC to fetch information
about incoming numbers from Odoo.

This project only contains a skeleton implementation and may require further
work to be production ready.
