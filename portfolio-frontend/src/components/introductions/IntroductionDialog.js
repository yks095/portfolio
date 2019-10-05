import { Button } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Slide from '@material-ui/core/Slide';
import IntroductionDelete from './IntroductionDelete';
import IntroductionEdit from './IntroductionEdit';

const styles = theme => ({
    panel: {
        marginBottom: '5px'
    },
    root: {
        width: '100%',
    },
    card: {
        minWidth: '100%',
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
    button: {
        margin: 10,
        width: '100%',
        height: '100%',
        fontSize: '300%',
        fontWeight: 'bold',
        fontFamily: 'Nanum Brush Script'

    }
});

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
});

class IntroductionDialog extends React.Component {
    constructor(props) {
        super();
        this.state = {
            introductionTitle: "",
            title1: "",
            content1: "",
            title2: "",
            content2: "",
            title3: "",
            content3: "",
            title4: "",
            content4: "",
            title5: "",
            content5: "",
            open: false
        }
    }

    handleClickOpen = () => {
        this.setState({
            open: true
        })
    }

    handleClickClose = () => {
        this.setState({
            open: false
        })
    }

    componentDidMount = () => {
        this.setState({
            introductionTitle: this.props.introductionTitle,
            title1: this.props.title1,
            content1: this.props.content1,
            title2: this.props.title2,
            content2: this.props.content2,
            title3: this.props.title3,
            content3: this.props.content3,
            title4: this.props.title4,
            content4: this.props.content4,
            title5: this.props.title5,
            content5: this.props.content5,
        })
    }

    render() {
        const { classes } = this.props;
        return (
            <div>
                <Button className={classes.button} onClick={this.handleClickOpen} variant="contained">{this.state.introductionTitle}</Button>
                <Dialog
                    fullWidth
                    maxWidth="lg"
                    open={this.state.open}
                    onClose={this.handleClose}
                    TransitionComponent={Transition}
                >
                    <div>
                        <DialogTitle>{this.props.title1}</DialogTitle>
                        <DialogContent>
                            <DialogContentText>
                                {this.props.content1}
                            </DialogContentText>
                        </DialogContent>
                    </div>
                    <div>
                        <DialogTitle>{this.props.title2}</DialogTitle>
                        <DialogContent>
                            <DialogContentText>
                                {this.props.content2}
                            </DialogContentText>
                        </DialogContent>
                    </div>
                    <div>
                        <DialogTitle>{this.props.title3}</DialogTitle>
                        <DialogContent>
                            <DialogContentText>
                                {this.props.content3}
                            </DialogContentText>
                        </DialogContent>
                    </div>
                    <div>
                        <DialogTitle>{this.props.title4}</DialogTitle>
                        <DialogContent>
                            <DialogContentText>
                                {this.props.content4}
                            </DialogContentText>
                        </DialogContent>
                    </div>
                    <div>
                        <DialogTitle>{this.props.title5}</DialogTitle>
                        <DialogContent>
                            <DialogContentText>
                                {this.props.content5}
                            </DialogContentText>
                        </DialogContent>
                    </div>
                    <DialogActions>
                        <IntroductionEdit
                            idx={this.props.idx}
                            introductionTitle={this.props.introductionTitle}
                            title1={this.props.title1}
                            content1={this.props.content1}
                            title2={this.props.title2}
                            content2={this.props.content2}
                            title3={this.props.title3}
                            content3={this.props.content3}
                            title4={this.props.title4}
                            content4={this.props.content4}
                            title5={this.props.title5}
                            content5={this.props.content5}
                        />
                        <IntroductionDelete
                            idx={this.props.idx}
                            introductionTitle={this.props.introductionTitle}
                            handleClickClose={this.handleClickClose}
                        />
                        <Button onClick={this.handleClickClose} color="primary" autoFocus>
                            Close
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

export default withStyles(styles)(IntroductionDialog);