# Demo Project For Job Application 

## Requirements:

1. Mysql
2. JDK 11
3. Spring IDE

## How to set up: 

1. Clone this repository by running the command below:
    ```shell
    git clone --depth 1 https://github.com/bryancabansay/parcel-cost-calculator-demo.git
    ```

2. Use the `dump.sql` file in the `db` directory to create and populate the database schema.

3. Open using your favorite Spring IDE (ItelliJ, Eclipse, etc.).

4. Update the properties file based on the correct database credentials.

5. Run the `CalculatorApplication.java` file as an application.

## Code Explanation:  

1. The database is used to store the rules so that it can be changed without stopping the server. This means that the user can add, remove, and update rules on the fly.

2. Javascript expressions are used to specify the condition for the rule using both weight and volume as variables. This means that if the user wants to change the condition of the Heavy Parcel, he or she can do so by writing something like `weight > 20 && volume > 3000`. If a parcel has a weight greater than 20 kg and volume greater than 3000 cubic cm, then it is a heavy parcel.

3. The API endpoints to update and create rules were not included in this demo since it would take too much time.

4. Please let me know if you have any questions.

## Notes: 

1. Open API spec file is found in the root directory. Please refer to api-docs.yaml.

2. The voucher API client was automatically generated by [swagger.io](https://editor.swagger.io/).
