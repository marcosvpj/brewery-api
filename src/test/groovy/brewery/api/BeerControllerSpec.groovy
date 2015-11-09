package brewery.api

import grails.test.mixin.*
import spock.lang.*

@TestFor(BeerController)
@Mock(Beer)
class BeerControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null

        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        assert false, "TODO: Provide a populateValidParams() implementation for this generated test suite"
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.beerList
            model.beerCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.beer!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def beer = new Beer()
            beer.validate()
            controller.save(beer)

        then:"The create view is rendered again with the correct model"
            model.beer!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            beer = new Beer(params)

            controller.save(beer)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/beer/show/1'
            controller.flash.message != null
            Beer.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def beer = new Beer(params)
            controller.show(beer)

        then:"A model is populated containing the domain instance"
            model.beer == beer
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def beer = new Beer(params)
            controller.edit(beer)

        then:"A model is populated containing the domain instance"
            model.beer == beer
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/beer/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def beer = new Beer()
            beer.validate()
            controller.update(beer)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.beer == beer

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            beer = new Beer(params).save(flush: true)
            controller.update(beer)

        then:"A redirect is issued to the show action"
            beer != null
            response.redirectedUrl == "/beer/show/$beer.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/beer/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def beer = new Beer(params).save(flush: true)

        then:"It exists"
            Beer.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(beer)

        then:"The instance is deleted"
            Beer.count() == 0
            response.redirectedUrl == '/beer/index'
            flash.message != null
    }
}
