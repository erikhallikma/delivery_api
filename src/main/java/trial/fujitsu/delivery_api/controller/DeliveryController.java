package trial.fujitsu.delivery_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trial.fujitsu.delivery_api.model.Delivery;

@RestController
@RequestMapping("/api")
public class DeliveryController {

    /**
     * Handles delivery price request
     *
     * @param request JSON object containing city, vehicle and optionally custom RBF, ATEF, WSEF, WPEF and timestamp values
     * @return ok response with delivery price
     */
    @PostMapping("/delivery")
    public ResponseEntity<Object> deliveryHandler(@RequestBody Delivery request) {
        return ResponseEntity.ok().body(request.getDeliveryPrice());
    }
}
