import React from 'react';
import { Link } from 'react-router'
import { render } from 'react-dom';

import { fetch } from '../utils';
import $ from 'jquery';

import InfoTable from './InfoTable';

export default class ClusterDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      cluster: {
        id: '',
        configs: [],
      },
      commands: [],
    };
  }

  componentDidMount() {
    this.loadData(this.props);
  }

  componentWillReceiveProps(nextProps) {
    this.loadData(nextProps);
  }

  loadData(props) {
    const { clusterUrl, commandsUrl } = props;
    $.when(fetch(clusterUrl), fetch(commandsUrl)).done((cluster, commands) => {
      this.setState({
        cluster: cluster[0],
        commands: commands[0],
      });
    });
  }

  render() {
    return(
      <tr>
        <td colSpan="7">
          <button
            type="button"
            className="close pull-left"
            onClick={(e) => this.props.hideDetails()}
            aria-label="Close"
          >
            <i className="fa fa-times" aria-hidden="true"></i>
          </button>
          <div className="job-detail-row">
          <table className="table job-detail-table">
            <tbody>
              <tr>
                <td className="col-xs-2">Description:</td>
                <td>{this.state.cluster.description}</td>
              </tr>
              <tr>
                <td className="col-xs-2">Setup File:</td>
                <td>{this.state.cluster.setupFile}</td>
              </tr>
              <tr>
                <td className="col-xs-2">Config:</td>
                <td>
                  <ul>
                    {this.state.cluster.configs.map((config, index) =>
                      <li key={index}>{config}</li>
                    )}
                  </ul>
               </td>
              </tr>
              <tr>
                <td className="col-xs-2">Commands:</td>
                <td>
                  {this.state.commands.length > 0 ?
                    <InfoTable data={this.state.commands} type="commands" />
                    :<div />
                  }
               </td>
              </tr>
              </tbody>
            </table>
            </div>
        </td>
      </tr>
    );
  }
}
