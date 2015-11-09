package brewery.api

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BeerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Beer.list(params), model:[beerCount: Beer.count()]
    }

    def show(Beer beer) {
        respond beer
    }

    def create() {
        respond new Beer(params)
    }

    @Transactional
    def save(Beer beer) {
        if (beer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (beer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond beer.errors, view:'create'
            return
        }

        beer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'beer.label', default: 'Beer'), beer.id])
                redirect beer
            }
            '*' { respond beer, [status: CREATED] }
        }
    }

    def edit(Beer beer) {
        respond beer
    }

    @Transactional
    def update(Beer beer) {
        if (beer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (beer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond beer.errors, view:'edit'
            return
        }

        beer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'beer.label', default: 'Beer'), beer.id])
                redirect beer
            }
            '*'{ respond beer, [status: OK] }
        }
    }

    @Transactional
    def delete(Beer beer) {

        if (beer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        beer.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'beer.label', default: 'Beer'), beer.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'beer.label', default: 'Beer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
