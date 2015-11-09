package brewery.api

class Beer {

	String name
	BeerType type

    static constraints = {
    }

    static static mappedBy = [type: "name"]
}
