import React from 'react';
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';

import {Stocks} from './Stocks';
import {AjaxStocksLoader} from "./AjaxStocksLoader";

window.startStocks = function() {
  let stocksLoader = new AjaxStocksLoader()
  const tpl = (
    <React.StrictMode>
      <div className="Stocks">
        <Stocks stocksLoader={stocksLoader}/>
      </div>
    </React.StrictMode>
  );
  ReactDOM.render(tpl, document.getElementById('pnl-stock'))
  ;
}

window.startStocks();

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
