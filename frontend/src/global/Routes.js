import React, { Component } from "react";
import { BrowserRouter as Router, Route, Switch, Redirect } from "react-router-dom";
import { connect } from "react-redux";

import { fetchUser } from "../actions";
import AppWrapper from './AppWrapper';
import NotFoundPage from '../pages/NotFoundPage';

class Routes extends Component {
  componentDidMount() {
    this.props.fetchUser();
  }

  render() {
    return (
      <Router>
        <AppWrapper>
          <Switch>
            <Route path="/404" component={NotFoundPage} />
          </Switch>
        </AppWrapper>
      </Router>
    );
  }
}

export default connect(
  null,
  { fetchUser }
)(Routes);
