package brewery.api

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BeerTypeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond BeerType.list(params), model:[beerTypeCount: BeerType.count()]
    }

    def show(BeerType beerType) {
        respond beerType
    }

    def create() {
        respond new BeerType(params)
    }

    @Transactional
    def save(BeerType beerType) {
        if (beerType == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (beerType.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond beerType.errors, view:'create'
            return
        }

        beerType.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'beerType.label', default: 'BeerType'), beerType.id])
                redirect beerType
            }
            '*' { respond beerType, [status: CREATED] }
        }
    }

    def edit(BeerType beerType) {
        respond beerType
    }

    @Transactional
    def update(BeerType beerType) {
        if (beerType == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (beerType.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond beerType.errors, view:'edit'
            return
        }

        beerType.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'beerType.label', default: 'BeerType'), beerType.id])
                redirect beerType
            }
            '*'{ respond beerType, [status: OK] }
        }
    }

    @Transactional
    def delete(BeerType beerType) {

        if (beerType == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        beerType.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'beerType.label', default: 'BeerType'), beerType.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'beerType.label', default: 'BeerType'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
