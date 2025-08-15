    package fpt.aptech.trackmentalhealth.entities;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    @Entity
    @Getter
    @Setter
    public class UserQuizAnswerItemOption {

        @EmbeddedId
        private UserQuizAnswerItemOptionId id = new UserQuizAnswerItemOptionId();

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("attemptId")
        @JoinColumn(name = "attempt_id")
        private UserQuizAttempt attempt;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("questionId")
        @JoinColumn(name = "question_id")
        private Question question;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("optionId")
        @JoinColumn(name = "option_id")
        private Option option;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumns({
                @JoinColumn(name = "attempt_id", referencedColumnName = "attempt_id", insertable = false, updatable = false),
                @JoinColumn(name = "question_id", referencedColumnName = "question_id", insertable = false, updatable = false)
        })
        private UserQuizAnswerItem answerItem;
    }