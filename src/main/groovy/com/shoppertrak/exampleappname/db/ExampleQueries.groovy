package com.shoppertrak.exampleappname.db

public class ExampleQueries {

    static final def EXAMPLE_QUERY = "SELECT" +
            " stuff" +
            " FROM TABLE(table_of_stuff.fnc_get_stuff(?, ?, ?, ?))"
}
