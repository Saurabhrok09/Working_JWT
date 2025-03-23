**401 vs. 403 Status Codes in Authentication & Authorization**

Status Code	Meaning	When Does It Occur?
Example Scenario
401 Unauthorized	User is not authenticated (invalid or missing credentials):
**When login credentials are incorrect or a JWT token is missing/invalid	User enters a wrong username/password during login**

403:
**Forbidden	User is authenticated but does not have permission	When the user is logged in but lacks access to a resource	A normal user tries to access an admin-only API**

For Your Test Case:
Since the credentials are invalid, authentication fails, and Spring Security will return 401 Unauthorized.

403 Forbidden would occur if a valid user logs in but tries to access an API they don’t have permissions for (e.g., a user tries to delete without the ADMIN role).
