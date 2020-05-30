import React from 'react';

import {Stock} from './Stock';

export class Stocks extends React.Component {

  constructor(props){
    super(props);
    this.props = props;
    this.state = {
      stocks: []
    };
    console.info("starting....");
  }

  componentDidMount(){
    console.info("mounted");
    let that = this;
    that.props.stocksLoader.start(function(stocks){
      that.setState({
        stocks: stocks
      })
    });
  }

  render(){
    return <div>
    <table className="table table-bordered table-striped">
      <thead className="thead-dark">
      <tr>
      <th scope="col">#</th>
      <th scope="col">Stock</th>
      <th scope="col">Price</th>
      </tr>
      </thead>
      <tbody>
      {
        this.state.stocks.map((v, k) => {
          return <Stock key={k} row={{k: k, v: v}} />
        })
      }
      </tbody>
      </table>
      </div>
  }
}
