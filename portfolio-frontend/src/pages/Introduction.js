import React from 'react';
import IntroductionPanel from '../components/IntroductionPanel';
import * as service from '../service/introduction';


class Introduction extends React.Component {

    constructor(props) {
        super();
        this.state = {
            introduction: {
                growth: null,
                reason: null,
                strength: null,
                weakness: null,
                aspiration: null
            }
        }
    }

    componentDidMount() {
        this.fetchIntroductionInfo();
    }

    fetchIntroductionInfo = async () => {
        const introduction = await service.getIntroduction();
        console.log(introduction);
        const {growth, reason, strength, weakness, aspiration} = introduction.data;
        this.setState({
            introduction: {
                growth,
                reason,
                strength,
                weakness,
                aspiration
            },
        });
    }

    render() {
        const {introduction} = this.state;
        return (
            <div>
                <IntroductionPanel
                    growth={introduction.growth}
                    reason={introduction.reason}
                    strength={introduction.strength}
                    weakness={introduction.weakness}
                    aspiration={introduction.aspiration}
                />
            </div >
        );
    }
}

export default Introduction;