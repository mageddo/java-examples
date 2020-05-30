import $ from "jquery";
import Url from './Url'

export class AjaxStocksLoader {
  start(callback) {
    this.intervalHandle = setInterval(function () {
      $
        .ajax({
          url: Url.solve("/api/stocks"),
          success: callback
        })
        // .error(function (error) {
        //   console.error("couldn't load stocks", error)
        // })
      ;
    }, 1000);
  }

  stop() {
    clearInterval(this.intervalHandle);
  }
}
