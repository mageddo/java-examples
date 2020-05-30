import React from 'react';
import {Stocks} from './Stocks';
import {AjaxStocksLoader} from "./AjaxStocksLoader";

export default {
  startStocks() {
    let stocksLoader = new AjaxStocksLoader()
    return (
      <div className="Stocks">
        <Stocks stocksLoader={stocksLoader}/>
      </div>
    );
  }
};
