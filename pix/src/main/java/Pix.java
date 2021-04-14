import com.emv.qrcode.core.model.mpm.TagLengthString;
import com.emv.qrcode.model.mpm.AdditionalDataField;
import com.emv.qrcode.model.mpm.AdditionalDataFieldTemplate;
import com.emv.qrcode.model.mpm.MerchantAccountInformationReservedAdditional;
import com.emv.qrcode.model.mpm.MerchantAccountInformationTemplate;
import com.emv.qrcode.model.mpm.MerchantPresentedMode;

public class Pix {
  public static void main(String[] args) {

    final var additionalDataField = getAddtionalDataField();
    final var accounts = getMerchantPaymentInfo();

    final var merchantPresentMode = new MerchantPresentedMode();
    merchantPresentMode.setAdditionalDataField(additionalDataField);
    merchantPresentMode.setCountryCode("BR");
    merchantPresentMode.setMerchantCategoryCode("0000");

    merchantPresentMode.setMerchantName("Elvis");
    merchantPresentMode.setMerchantCity("SP");
    merchantPresentMode.setPayloadFormatIndicator("01");
    merchantPresentMode.setPointOfInitiationMethod("11");

    merchantPresentMode.setTransactionAmount("1.99");
    merchantPresentMode.setTransactionCurrency("986");

    merchantPresentMode.addMerchantAccountInformation(accounts);
    System.out.println(merchantPresentMode.toString());
  }

  // Merchant Account Information Template (IDs "26" to "51")
  private static MerchantAccountInformationTemplate getMerchantPaymentInfo() {
    final var info = new MerchantAccountInformationReservedAdditional();
    info.setGloballyUniqueIdentifier("BR.GOV.BCB.PIX");
    // it can be an email, cpf, phone, etc.
    info.addPaymentNetworkSpecific(new TagLengthString("01", "edigitalb@gmail"));
    return new MerchantAccountInformationTemplate("26", info);
  }


  private static AdditionalDataFieldTemplate getAddtionalDataField() {
    final var additionalDataFieldValue = new AdditionalDataField();
    additionalDataFieldValue.setReferenceLabel("***");

    final var additionalDataField = new AdditionalDataFieldTemplate();
    additionalDataField.setValue(additionalDataFieldValue);

    return additionalDataField;
  }
}
