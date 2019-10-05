import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import { withStyles } from '@material-ui/styles';
import React from 'react';
import DialogFull from '../components/DialogFull';
import ProjectDialog from '../components/projects/ProjectDialog';
import ProjectAdd from '../components/projects/ProjectAdd';
import * as service from '../service/projects';
import './Projects.css';

const styles = (theme) => ({
    root: {
        marginTop: 100,
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent: 'center',
        overflow: 'hidden',
    },
    button: {
        fontSize: '200%',
        fontWeight: 'bold',
    },
    gridList: {
        width: 1000,
        float: 'center',
        height: 550,
        display: 'flex',
    },
    gridText: {
        textAlign: 'center',
        float: 'center',
        verticalAlign: 'middle'
    },
});

class Projects extends React.Component {
    constructor(props) {
        super();
        this.state = {
            projects: "",
        }
    }

    componentDidMount() {
        this.fetchIntroductionInfo();
    }

    fetchIntroductionInfo = async () => {
        const projects = await service.getProjects();
        console.log(projects.data)
        this.setState({
            'projects': projects.data._embedded.projectList
        });
    }
    render() {
        const { classes } = this.props;
        return (
            <div >
                <div className="Projects-Background">
                    <DialogFull />
                    <div className="Title_Text_I">Projects</div>
                    <ProjectAdd />
                    <div className={classes.root}>
                        <GridList cellHeight={250} className={classes.gridList} cols={3}>
                            {
                                this.state.projects ?
                                    this.state.projects.map(c => {
                                        return (
                                            <GridListTile key={c.idx} cols={c.cols || 1}>
                                                <ProjectDialog
                                                    key={c.idx}
                                                    onClick={this.handleClickOpen}
                                                    idx={c.idx}
                                                    name={c.name}
                                                    description={c.description}
                                                    image={c.image}
                                                    period={c.period}
                                                    git_addr={c.git_addr}
                                                    registeredDate={c.registeredDate} >
                                                    {c.title}
                                                </ProjectDialog>
                                            </GridListTile>
                                        )
                                    }) : ""
                            }
                        </GridList>
                    </div>
                </div>
            </div>
        );
    }
}

export default withStyles(styles)(Projects);