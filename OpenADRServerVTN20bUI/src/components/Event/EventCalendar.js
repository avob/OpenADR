import React from 'react';

import Typography from '@material-ui/core/Typography';

export class EventCalendar extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
    const {classes } = this.props;

    return (
      <div className={ classes.root }>
        <Typography gutterBottom
                    variant="title"
                    component="h2">
          EventCalendar
        </Typography>
     
      </div>
    );
  }
}

export default EventCalendar;
