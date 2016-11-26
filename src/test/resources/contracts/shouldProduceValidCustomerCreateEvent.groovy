package contracts

import org.springframework.cloud.contract.spec.Contract
import org.springframework.util.MimeType


Contract.make {
    label 'create-customer'
    input {
        triggeredBy('postCustomer()')
    }
    outputMessage {
        sentTo('customer')
        headers({ header('contentType': MimeType.valueOf('application/json;charset=UTF-8')) })
        body('''{"id":"foo", "name":"John", "email":"john@example.com"}''')
    }
}