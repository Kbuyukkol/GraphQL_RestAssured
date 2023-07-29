package spacex.queries;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import spacex.GraphQLQuery;
import spacex.QueryLimit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BasicQueries {

    @Test
    public void getCompanyData() {

        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("{ company { founder } }");

        given().
                contentType(ContentType.JSON).
                body(query).
                when().
                post("https://spacex-production.up.railway.app/").
                then().
                assertThat().
                statusCode(200).
                and().
                body("data.company.founder", equalTo("Elon Musk"));
    }

    @Test
    public void getRocketsData() {

        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("{ rockets { country, id } }");

        given().
                contentType(ContentType.JSON).
                body(query).
                when().
                post("https://spacex-production.up.railway.app/").
                then().
                assertThat().
                statusCode(200).
                and().
                log().all().
                body("data.rockets[0].country", equalTo("Republic of the Marshall Islands")
                        , "data.rockets[0].id", equalTo("5e9d0d95eda69955f709d1eb"));

    }

    @Test
    public void getLaunchesData() {

        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("query { launches(limit: 10) { mission_name } }");

        given().
                contentType(ContentType.JSON).
                body(query).
                when().
                post("https://spacex-production.up.railway.app/").
                then().
                assertThat().
                statusCode(200).
                and().
                body("data.launches[0].mission_name", equalTo("FalconSat"));
    }



    @Test
    public void getLaunchesDataWithPOJO() {

        GraphQLQuery query = new GraphQLQuery();
        query.setQuery("query getLaunches($limit: Int!){ launches(limit: $limit) { mission_name } }");

        QueryLimit queryLimit = new QueryLimit();
        queryLimit.setLimit(10);

        query.setVariables(queryLimit);

        given().
                contentType(ContentType.JSON).
                body(query).
                when().
                post("https://spacex-production.up.railway.app/").
                then().
                assertThat().
                statusCode(200).
                and().
                body("data.launches[1].mission_name", equalTo("DemoSat"));
    }

}