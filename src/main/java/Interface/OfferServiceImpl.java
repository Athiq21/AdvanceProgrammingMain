package Interface;

import model.Offer;
import javax.mail.Part;
import java.io.IOException;
import java.util.List;

public interface OfferServiceImpl {
    Offer saveOffer(Offer offer);
    List <Offer>getAllOffers();
}