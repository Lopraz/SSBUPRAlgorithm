package utils;

public class GraphQLQuery {
    private String query;
    private String variables;

    public GraphQLQuery(String query, String variables) {
        this.query = query;
        this.variables = variables;
    }

    public GraphQLQuery() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }
}
